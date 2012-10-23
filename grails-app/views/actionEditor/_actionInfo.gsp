<%@ page import="game.UsableItem; game.MapTransition; game.Item; game.MarketAction; game.Pokemon; game.OwnerPokemon; game.NpcAction; game.MapMessage" %>
<div>
    <g:link action="deleteAction" id="${action.id}">Delete Action</g:link>

    <g:if test="${action in MapTransition}">
        <g:form action="updateAction">
            <table>

                <g:render template="basicActionInfo" />

                <tr>
                    <td>

                    </td>
                    <td>
                        <g:submitButton name="save" />
                    </td>
                </tr>
            </table>
        </g:form>
    </g:if>

    <g:elseif test="${action in MarketAction}">
        <table>

            <g:render template="basicActionInfo" />
            <tr>
                <td>
                    MarketItems:
                </td>
                <td>
                    <table>
                        <g:each var="marketItem" in="${action.market.marketItems}">
                            <tr>
                                <td>${marketItem.item.name}</td>
                            </tr>
                        </g:each>
                    </table>
                    <g:form action="addItem">
                        <g:hiddenField name="market" value="${action.market.id}" />
                        <g:select name="item" from="${UsableItem.list()}" optionKey="id" />
                        <g:submitButton name="add" />
                    </g:form>
                </td>
            </tr>

        </table>
    </g:elseif>

    <g:elseif test="${action in MapMessage}">
        <g:form action="updateAction">
            <table>

                <g:render template="basicActionInfo" />
                <tr>
                    <td>
                        Message:
                    </td>
                    <td>
                        <g:textField name="message" value="${action.message}" />
                    </td>
                </tr>

                <tr>
                    <td>

                    </td>
                    <td>
                        <g:submitButton name="save" />
                    </td>
                </tr>

            </table>
        </g:form>
    </g:elseif>
    <g:elseif test="${action in NpcAction}">
        <g:form action="updateAction">

            <table>

                <g:render template="basicActionInfo" />
                <tr>
                    <td>
                        NPC Name:
                    </td>
                    <td>
                        <g:checkBox name="owner.name" value="${action.owner.name}" />
                    </td>
                </tr>
                <tr>
                    <td>
                        Permanent Lock:
                    </td>
                    <td>
                        <g:checkBox name="owner.permanentLock" value="${action.owner.permanentLock}" />
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <g:submitButton name="save" />
                    </td>
                </tr>

            </table>
        </g:form>

        <table>
            <tr>
                <td>
                    Pokemon:
                </td>
                <td>
                    <table>
                        <%
                            List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByOwner(action.owner)
                        %>
                        <tr>
                            <td>Pokemon</td>
                            <td>Level</td>
                        </tr>
                        <g:each in="${ownerPokemonList}" var="ownerPokemon">
                            <tr>
                                <td>${ownerPokemon.pokemon}</td>
                                <td>${ownerPokemon.level}</td>
                            </tr>
                        </g:each>

                    </table>

                    <g:form action="addPokemonToNpc">
                        <g:hiddenField name="owner" value="${action.owner.id}" />
                        <table>
                            <tr>
                                <td>
                                    Pokemon:
                                </td>
                                <td>
                                    <g:select name="pokemon" from="${Pokemon.list()}" optionKey="id" />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Level:
                                </td>
                                <td>
                                    <g:textField name="level" />
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td><g:submitButton name="add" /></td>
                            </tr>
                        </table>
                    </g:form>
                </td>
            </tr>
        </table>

    </g:elseif>
    <g:else>
        <table>
            <g:render template="basicActionInfo" />
        </table>
    </g:else>

</div>
