<%@ page import="game.item.UsableItem; game.item.Item; game.item.Item; game.action.FindItemAction; game.action.MapTransition; game.action.MarketAction; game.action.NpcAction; game.Pokemon; game.OwnerPokemon; game.action.MapMessage" %>
<div>
    <g:link action="deleteAction" id="${action.id}">Delete Action</g:link>

    <g:if test="${action in game.action.MapTransition}">
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

    <g:elseif test="${action in game.action.MarketAction}">
        <g:form action="updateAction">
            <table>
                <g:render template="basicActionInfo" />
            </table>
        </g:form>

        <h3>Market Items</h3>

        <table>
            <tr>
                <td>
                    Item
                </td>
                <td>
                    Cost
                </td>
            </tr>
            <g:each var="marketItem" in="${action.market.marketItems}">
                <tr>
                    <td>${marketItem.item.name}</td>
                    <td>${marketItem.item.cost}</td>
                    <td><g:link action="removeMarketItem" id="${marketItem.id}">Delete</g:link></td>
                </tr>
            </g:each>
        </table>
        <g:form action="addItem">
            <g:hiddenField name="market" value="${action.market.id}" />
            <g:select name="item" from="${game.item.UsableItem.list()}" optionKey="id" />
            <g:submitButton name="add" />
        </g:form>

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
    <g:elseif test="${action in game.action.NpcAction}">
        <g:form action="updateAction">

            <table>

                <g:render template="basicActionInfo" />
                <tr>
                    <td>
                        NPC Name:
                    </td>
                    <td>
                        <g:textField name="owner.name" value="${action.owner.name}" />
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
                    <td>
                        Locked Message:
                    </td>
                    <td>
                        <g:textField name="owner.npcLockedMessage" value="${action.owner.npcLockedMessage}" />
                    </td>
                </tr>
                <tr>
                    <td>
                        Defeated Message:
                    </td>
                    <td>
                        <g:textField name="owner.npcDefeatedMessage" value="${action.owner.npcDefeatedMessage}" /> In battle message. You should seperate lines with a ';' karakter.
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

        <h3>Pokemon</h3>
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

        <h3>Reward Items</h3>
        <table>
            <tr>
                <td>Item</td>
                <td>Quantity</td>
            </tr>
            <g:each in="${action.owner.rewardItems}" var="ownerItem">
                <tr>
                    <td>
                        ${ownerItem.item.name}
                    </td>
                    <td>
                        ${ownerItem.quantity}
                    </td>
                </tr>

            </g:each>
        </table>

        <g:form action="addRewardItemToNpc">
            <g:hiddenField name="owner" value="${action.owner.id}" />
            <table>
                <tr>
                    <td>
                        Item
                    </td>
                    <td>
                        <g:select name="item" optionValue="${ { it.name } }" from="${game.item.Item.list()}" optionKey="id" />
                    </td>
                </tr>
                <tr>
                    <td>
                        Quantity
                    </td>
                    <td>
                        <g:textField name="quantity" value="1" />
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td><g:submitButton name="add" /></td>
                </tr>
            </table>
        </g:form>

    </g:elseif>
    <g:elseif test="${action in game.action.FindItemAction}">
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
        <h3>Reward Items</h3>
        <table>
            <tr>
                <td>Item</td>
                <td>Quantity</td>
            </tr>
            <g:each in="${action.rewardItems}" var="ownerItem">
                <tr>
                    <td>
                        ${ownerItem.item.name}
                    </td>
                    <td>
                        ${ownerItem.quantity}
                    </td>
                </tr>

            </g:each>
        </table>

        <g:form action="addRewardItem">
            <g:hiddenField name="actionId" value="${action.id}" />
            <table>
                <tr>
                    <td>
                        Item
                    </td>
                    <td>
                        <g:select name="item" optionValue="${ { it.name } }" from="${game.item.Item.list()}" optionKey="id" />
                    </td>
                </tr>
                <tr>
                    <td>
                        Quantity
                    </td>
                    <td>
                        <g:textField name="quantity" value="1" />
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td><g:submitButton name="add" /></td>
                </tr>
            </table>
        </g:form>

    </g:elseif>
    <g:elseif test="${action in game.action.CharacterAction}">
        <g:form action="updateAction">

            <table>
                <g:render template="basicActionInfo" />

                <tr>
                    <td>
                        Macro:
                    </td>
                    <td>
                        <g:textField name="macro" value="${action.macro}" />
                    </td>
                </tr>
                <g:if test="${action in game.action.MessagePersonAction}">
                    <tr>
                        <td>
                            Message:
                        </td>
                        <td>
                            <g:textField name="message" value="${action.message}" />
                        </td>
                    </tr>
                </g:if>

                <g:if test="${action in game.action.PersonAction}">
                    <tr>
                        <td>
                            CharacterImage:
                        </td>
                        <td>
                            <g:select name="characterImage" from="${map.CharacterImage.values()}" value="${action.characterImage}" />
                        </td>
                    </tr>
                </g:if>
                <g:if test="${action in game.action.PokemonAction}">
                    <tr>
                        <td>
                            Pokemon:
                        </td>
                        <td>
                            <g:select name="pokemonNr" from="${(1..150)}" value="${action.pokemonNr}" />
                        </td>
                    </tr>
                </g:if>
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
    <g:else>
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
    </g:else>

</div>
