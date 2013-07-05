function choose()
{
    pkmn = $("input[name='pokemon']:checked").val();

    if(pkmn != "undefined" && pkmn != undefined)
    {
        $.ajax({
            type: "GET",
            url: serverUrl + "/choosePokemon/choose",
            data: "pkmn="+pkmn,
            cache: false,
            success: function(msg){
                getView();
            }
        });
    }
    else
    {
        alert("Choose a Pokemon");
    }
}