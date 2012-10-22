<div style="text-align:left;padding-left:20px;">
    Money: ${money}

    <g:render template="marketItems" model="${[marketItems:marketItems]}" />
    <p>
        <a href='${createLink(action:'exit')}'>Exit</a>
    </p>
</div>