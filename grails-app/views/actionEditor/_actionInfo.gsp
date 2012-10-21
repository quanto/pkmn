<%@ page import="game.Item; game.MarketAction; game.Pokemon; game.OwnerPokemon; game.NpcAction; game.MapMessage" %>
<div>
    <table>
        <tr>
            <td>class:</td>
            <td>${action.class}</td>
        </tr>
        <tr>
            <td>positionX:</td>
            <td>${action.positionX}</td>
        </tr>
        <tr>
            <td>positionY:</td>
            <td>${action.positionY}</td>
        </tr>
        <g:if test="${action in MarketAction}">
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
                        <g:select name="item" from="${Item.list()}" optionKey="id" />
                        <g:submitButton name="add" />
                    </g:form>
                </td>
            </tr>
        </g:if>

        <g:if test="${action in MapMessage}">
            <tr>
                <td>
                    Message:
                </td>
                <td>
                    ${action.message}
                </td>
            </tr>
        </g:if>
        <g:elseif test="${action in NpcAction}">
             <tr>
                 <td>
                    NPC Name:
                 </td>
                 <td>
                    ${action.owner.name}
                 </td>
             </tr>
            <tr>
                <td>
                    Permanent Lock:
                </td>
                <td>
                    <g:form action="editNpc">
                        <g:hiddenField name="owner" value="${action.owner.id}" />
                        <g:checkBox name="permanentLock" value="${action.owner.permanentLock}" />
                        <g:submitButton name="save" />
                    </g:form>
                </td>
            </tr>
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
        </g:elseif>
    </table>
</div>
