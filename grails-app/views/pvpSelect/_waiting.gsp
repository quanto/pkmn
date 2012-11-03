<h1>Waiting for someone to accept</h1>

<g:link action="cancelInvite">Cancel invite</g:link>

<script type="text/javascript">
    var interval = setInterval(function(){
        if (checkBattle()){
            clearInterval(interval)
        }
    },2000)
</script>

<p>
    <a href='${createLink(action:'exit')}'>Exit</a>
</p>