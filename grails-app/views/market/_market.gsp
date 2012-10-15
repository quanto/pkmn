Money: ${money}

<g:render template="ownerItems" model="${[ownerItems:ownerItems]}" />

<g:render template="marketItems" model="${[marketItems:marketItems]}" />

<a href='${createLink(action:'exit')}'>Exit</a>