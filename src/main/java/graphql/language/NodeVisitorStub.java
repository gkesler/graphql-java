/*
 * Copyright 2016 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 * 
 * graphql.language.NodeVisitorStub.java
 * 
 * Created: Oct 30, 2017 6:31:32 PM
 * Author: gkesler
 */
package graphql.language;

/**
 *
 * @author gkesler
 */
public abstract class NodeVisitorStub<U> implements NodeVisitor<U> {
    @Override
    public U visit(Argument node, U data) {
        return visitNode(node, data);
    }

    @Override
    public U visit(ArrayValue node, U data) {
        return visitValue(node, data);
    }

    @Override
    public U visit(BooleanValue node, U data) {
        return visitValue(node, data);
    }

    @Override
    public U visit(Directive node, U data) {
        return visitNode(node, data);
    }

    @Override
    public U visit(DirectiveDefinition node, U data) {
        return visitDefinition(node, data);
    }

    @Override
    public U visit(DirectiveLocation node, U data) {
        return visitNode(node, data);
    }

    @Override
    public U visit(Document node, U data) {
        return visitNode(node, data);
    }

    @Override
    public U visit(EnumTypeDefinition node, U data) {
        return visitTypeDefinition(node, data);
    }

    @Override
    public U visit(EnumValue node, U data) {
        return visitValue(node, data);
    }

    @Override
    public U visit(EnumValueDefinition node, U data) {
        return visitNode(node, data);
    }

    @Override
    public U visit(Field node, U data) {
        return visitSelection(node, data);
    }

    @Override
    public U visit(FieldDefinition node, U data) {
        return visitNode(node, data);
    }

    @Override
    public U visit(FloatValue node, U data) {
        return visitValue(node, data);
    }

    @Override
    public U visit(FragmentDefinition node, U data) {
        return visitDefinition(node, data);
    }

    @Override
    public U visit(FragmentSpread node, U data) {
        return visitSelection(node, data);
    }

    @Override
    public U visit(InlineFragment node, U data) {
        return visitSelection(node, data);
    }

    @Override
    public U visit(InputObjectTypeDefinition node, U data) {
        return visitTypeDefinition(node, data);
    }

    @Override
    public U visit(InputValueDefinition node, U data) {
        return visitNode(node, data);
    }

    @Override
    public U visit(IntValue node, U data) {
        return visitValue(node, data);
    }

    @Override
    public U visit(InterfaceTypeDefinition node, U data) {
        return visitTypeDefinition(node, data);
    }

    @Override
    public U visit(ListType node, U data) {
        return visitType(node, data);
    }

    @Override
    public U visit(NonNullType node, U data) {
        return visitType(node, data);
    }

    @Override
    public U visit(NullValue node, U data) {
        return visitValue(node, data);
    }

    @Override
    public U visit(ObjectField node, U data) {
        return visitNode(node, data);
    }

    @Override
    public U visit(ObjectTypeDefinition node, U data) {
        return visitTypeDefinition(node, data);
    }

    @Override
    public U visit(ObjectValue node, U data) {
        return visitValue(node, data);
    }

    @Override
    public U visit(OperationDefinition node, U data) {
        return visitDefinition(node, data);
    }

    @Override
    public U visit(OperationTypeDefinition node, U data) {
        return visitNode(node, data);
    }

    @Override
    public U visit(ScalarTypeDefinition node, U data) {
        return visitTypeDefinition(node, data);
    }

    @Override
    public U visit(SchemaDefinition node, U data) {
        return visitDefinition(node, data);
    }

    @Override
    public U visit(SelectionSet node, U data) {
        return visitNode(node, data);
    }

    @Override
    public U visit(StringValue node, U data) {
        return visitValue(node, data);
    }

    @Override
    public U visit(TypeName node, U data) {
        return visitType(node, data);
    }

    @Override
    public U visit(UnionTypeDefinition node, U data) {
        return visitTypeDefinition(node, data);
    }

    @Override
    public U visit(VariableDefinition node, U data) {
        return visitNode(node, data);
    }

    @Override
    public U visit(VariableReference node, U data) {
        return visitValue(node, data);
    }
    
    protected U visitNode (Node node, U data) {
        return data;
    }
    
    protected U visitValue (Value node, U data) {
        return visitNode(node, data);
    }
    
    protected U visitDefinition (Definition node, U data) {
        return visitNode(node, data);
    }
    
    protected U visitTypeDefinition (TypeDefinition node, U data) {
        return visitNode(node, data);
    }
    
    protected U visitSelection (Selection node, U data) {
        return visitNode(node, data);
    }
    
    protected U visitType (Type node, U data) {
        return visitNode(node, data);
    }

    @Override
    public U leave(Node node, U data) {
        return data;
    }
}
