package graphql.execution;


import graphql.Internal;
import graphql.language.Field;
import graphql.language.FragmentDefinition;
import graphql.language.FragmentSpread;
import graphql.language.InlineFragment;
import graphql.language.Selection;
import graphql.language.SelectionSet;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLType;
import graphql.schema.GraphQLUnionType;
import graphql.schema.SchemaUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static graphql.execution.TypeFromAST.getTypeFromAST;
import graphql.language.LanguageUtil;
import graphql.language.LanguageUtil.Markers;
import graphql.language.NodeVisitorStub;
import graphql.schema.GraphQLTypeVisitorStub;

/**
 * A field collector can iterate over field selection sets and build out the sub fields that have been selected,
 * expanding named and inline fragments as it goes.s
 */
@Internal
public class FieldCollector {

    private final ConditionalNodes conditionalNodes;

    private final SchemaUtil schemaUtil = new SchemaUtil();

    public FieldCollector() {
        conditionalNodes = new ConditionalNodes();
    }


    /**
     * Given a list of fields this will collect the sub-field selections and return it as a map
     *
     * @param parameters the parameters to this method
     * @param fields     the list of fields to collect for
     *
     * @return a map of the sub field selections
     */
    public Map<String, List<Field>> collectFields(FieldCollectorParameters parameters, List<Field> fields) {
        Map<String, List<Field>> subFields = new LinkedHashMap<>();
        List<String> visitedFragments = new ArrayList<>();
        for (Field field : fields) {
            if (field.getSelectionSet() == null) {
                continue;
            }
            this.collectFields(parameters, field.getSelectionSet(), visitedFragments, subFields);
        }
        return subFields;
    }

    /**
     * Given a selection set this will collect the sub-field selections and return it as a map
     *
     * @param parameters   the parameters to this method
     * @param selectionSet the selection set to collect on
     *
     * @return a map of the sub field selections
     */
    public Map<String, List<Field>> collectFields(FieldCollectorParameters parameters, SelectionSet selectionSet) {
        Map<String, List<Field>> subFields = new LinkedHashMap<>();
        List<String> visitedFragments = new ArrayList<>();
        this.collectFields(parameters, selectionSet, visitedFragments, subFields);
        return subFields;
    }

    private void collectFields(FieldCollectorParameters parameters, SelectionSet selectionSet, List<String> visitedFragments, Map<String, List<Field>> fields) {
        LanguageUtil.depthFirst(selectionSet.getSelections(), null, new NodeVisitorStub<Object>() {
            @Override
            public Object visit(InlineFragment node, Object data) {
                return collectInlineFragment(parameters, visitedFragments, fields, node);
            }

            @Override
            public Object visit(FragmentSpread node, Object data) {
                return collectFragmentSpread(parameters, visitedFragments, fields, node);
            }

            @Override
            public Object visit(Field node, Object data) {
                return collectField(parameters, fields, node);
            }

            private Object collectFragmentSpread(FieldCollectorParameters parameters, List<String> visitedFragments, Map<String, List<Field>> fields, FragmentSpread fragmentSpread) {
                do {
                    if (visitedFragments.contains(fragmentSpread.getName())) {
                        break;
                    }
                    if (!conditionalNodes.shouldInclude(parameters.getVariables(), fragmentSpread.getDirectives())) {
                        break;
                    }
                    visitedFragments.add(fragmentSpread.getName());
                    FragmentDefinition fragmentDefinition = parameters.getFragmentsByName().get(fragmentSpread.getName());

                    if (!conditionalNodes.shouldInclude(parameters.getVariables(), fragmentDefinition.getDirectives())) {
                        break;
                    }
                    if (!doesFragmentConditionMatch(parameters, fragmentDefinition)) {
                        break;
                    }

                    LanguageUtil.depthFirst(fragmentDefinition.getSelectionSet().getSelections(), null, this);
                    return null;
                } while (false);
                
                // abort recursion
                return Markers.STOP;
            }

            private Object collectInlineFragment(FieldCollectorParameters parameters, List<String> visitedFragments, Map<String, List<Field>> fields, InlineFragment inlineFragment) {
                do {
                    if (!conditionalNodes.shouldInclude(parameters.getVariables(), inlineFragment.getDirectives()) ||
                            !doesFragmentConditionMatch(parameters, inlineFragment)) {
                        break;
                    }

                    return null;
                } while (false);
                
                // abort recursion
                return Markers.STOP;
            }

            private Object collectField(FieldCollectorParameters parameters, Map<String, List<Field>> fields, Field field) {
                do {
                    if (!conditionalNodes.shouldInclude(parameters.getVariables(), field.getDirectives())) {
                        break;
                    }

                    List<Field> groupped = new ArrayList<>();
                    nvl(fields.putIfAbsent(getFieldEntryKey(field), groupped), groupped)
                        .add(field);
                } while (false);
                
                // abort recursion
                return Markers.STOP;
            }
        });
    }

    private static <T> T nvl (T value, T defaultValue) {
        return (value == null)
            ? defaultValue
            : value;
    }

    private static String getFieldEntryKey(Field field) {
        if (field.getAlias() != null) return field.getAlias();
        else return field.getName();
    }

    // fIXME: shall be static method
    private boolean doesFragmentConditionMatch(FieldCollectorParameters parameters, InlineFragment inlineFragment) {
        if (inlineFragment.getTypeCondition() == null) {
            return true;
        }
        GraphQLType conditionType;
        conditionType = getTypeFromAST(parameters.getGraphQLSchema(), inlineFragment.getTypeCondition());
        return checkTypeCondition(parameters, conditionType);
    }

    // fIXME: shall be static method
    private boolean doesFragmentConditionMatch(FieldCollectorParameters parameters, FragmentDefinition fragmentDefinition) {
        GraphQLType conditionType;
        conditionType = getTypeFromAST(parameters.getGraphQLSchema(), fragmentDefinition.getTypeCondition());
        return checkTypeCondition(parameters, conditionType);
    }

    // fIXME: shall be static method
    private boolean checkTypeCondition(FieldCollectorParameters parameters, GraphQLType conditionType) {
        GraphQLObjectType type = parameters.getObjectType();
        if (conditionType.equals(type)) {
            return true;
        }

        return conditionType.accept(new GraphQLTypeVisitorStub<Boolean>() {
            @Override
            public Boolean visit(GraphQLUnionType conditionType, Boolean data) {
                return conditionType.getTypes().contains(type);
            }

            @Override
            public Boolean visit(GraphQLInterfaceType conditionType, Boolean data) {
                List<GraphQLObjectType> implementations = schemaUtil.findImplementations(parameters.getGraphQLSchema(), conditionType);
                return implementations.contains(type);
            }
        }, false);
    }


}
