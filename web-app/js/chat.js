function updateChat()
{
    $.ajax({
        type: "GET",
        url: serverUrl + "/chat/index",
        data: "",
        cache: false,
        success: function(chatMessages){
            $("#chatBox").html(chatMessages);
            el = document.getElementById("chatBox");
            el.scrollTop = el.scrollHeight;
        }
    });
};

function sendChatMessage()
{
    message = $("#chatMessage").attr("value");
    if(message != "")
    {
        $("#chatMessage").attr({value: ""});
        $.ajax({
            type: "POST",
            url: serverUrl + "/chat/send",
            data: "chatMessage="+message,
            success: function(chatMessages){
                updateChat();
            }
        });
    }
};