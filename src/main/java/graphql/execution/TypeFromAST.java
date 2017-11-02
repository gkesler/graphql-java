package graphql.execution;


import graphql.Internal;
import graphql.language.LanguageUtil;
import graphql.language.ListType;
import graphql.language.Node;
import graphql.language.NodeVisitor;
import graphql.language.NodeVisitorStub;
import graphql.language.NonNullType;
import graphql.language.Type;
import graphql.language.TypeName;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;
import java.util.Collections;

@Internal
public class TypeFromAST {
    private TypeFromAST () {
    }
    
    public static GraphQLType getTypeFromAST(GraphQLSchema schema, Type type) {
        return LanguageUtil.depthFirst(Collections.singleton(type), null, new NodeVisitorStub<GraphQLType>() {
            @Override
            public GraphQLType visit(TypeName node, GraphQLType data) {
                return schema.getType(node.getName());
            }

            @Override
            public GraphQLType leave(Node node, GraphQLType data) {
                return node.accept(TYPE_WRAPPER, data);
            }
        });
    }
    
    private static final NodeVisitor<GraphQLType> TYPE_WRAPPER = new NodeVisitorStub<GraphQLType>() {
        @Override
        public GraphQLType visit(NonNullType node, GraphQLType data) {
            return new GraphQLNonNull(data);
        }

        @Override
        public GraphQLType visit(ListType node, GraphQLType data) {
            return new GraphQLList(data);
        }
    };
}
