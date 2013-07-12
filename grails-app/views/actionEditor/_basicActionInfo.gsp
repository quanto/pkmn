<tr>
    <td>class:</td>
    <td>
        ${action.class}
        <g:hiddenField name="actionId" value="${action.id}" />
    </td>
</tr>
<tr>
    <td>positionX:</td>
    <td>
        <g:textField name="positionX" value="${action.positionX}" />
    </td>
</tr>
<tr>
    <td>positionY:</td>
    <td>
        <g:textField name="positionY" value="${action.positionY}" />
    </td>
</tr>
<tr>
    <td>
        Condition:
    </td>
    <td>
        <g:select noSelection="['':'']" from="${game.Condition.values()}" name="condition" value="${action.condition}" />
    </td>
</tr>
<tr>
    <td>
        ConditionMetMessage:
    </td>
    <td>
        <g:textField name="conditionMetMessage" value="${action.conditionMetMessage}" />
    </td>
</tr>
<tr>
    <td>
        ConditionNotMetMessage:
    </td>
    <td>
        <g:textField name="conditionNotMetMessage" value="${action.conditionNotMetMessage}" />
    </td>
</tr>
<tr>
    <td>
        TriggerOnActionButton:
    </td>
    <td>
        <g:checkBox name="triggerOnActionButton" value="${action.triggerOnActionButton}" /> Trigger the action on an action call
    </td>
</tr>
<tr>
    <td>
        TriggerBeforeStep:
    </td>
    <td>
        <g:checkBox name="triggerBeforeStep" value="${action.triggerBeforeStep}" /> Trigger the action before a step is done
    </td>
</tr>
<tr>
    <td>
        ConditionalStep:
    </td>
    <td>
        <g:checkBox name="conditionalStep" value="${action.conditionalStep}" /> This option only make sense when u use the TriggerBeforeStep. If the condition is not met, the step is not allowed.
    </td>
</tr>
<tr>
    <td>
        PlaceOneTimeActionLock:
    </td>
    <td>
        <g:checkBox name="placeOneTimeActionLock" value="${action.placeOneTimeActionLock}" />
    </td>
</tr>
