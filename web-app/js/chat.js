
var lastChatId = '0';

function updateChat()
{
    $chatbox = $("#chatBox");
    var maxMessages = 100;

    $.ajax({
        type: "GET",
        url: serverUrl + "/chat/index",
        data: "lastChatId="+lastChatId,
        cache: false,
        success: function(chatMessages){
            $chatbox.append(chatMessages);

            var $messages = $chatbox.find('div')
            if ($messages.length > maxMessages){
                $messages.slice(0,$messages.length - maxMessages).remove()
            }

            var el = document.getElementById("chatBox");
            el.scrollTop = el.scrollHeight;
        }
    });
};

function setPrivateMessage(username){
    $('#chatMessage').val('@' +username + ' ');
    $('#chatMessage').putCursorAtEnd();
}

function sendChatMessage()
{
    message = $("#chatMessage").attr("value");
    if(message != "")
    {
        $("#chatMessage").attr({value: ""});
        $.ajax({
            type: "POST",
            url: serverUrl + "/chat/send",
            data: "chatScope=" + $("#chatScope").val() + "&mapId=" + mapId + "&message="+message,
            success: function(chatMessages){
                updateChat();
            }
        });
    }
};

(function($)
{
    jQuery.fn.putCursorAtEnd = function()
    {
        return this.each(function()
        {
            $(this).focus()

            if (this.setSelectionRange)
            {
                var len = $(this).val().length * 2;
                this.setSelectionRange(len, len);
            }
            else
            {
                $(this).val($(this).val());
            }
            this.scrollTop = 999999;
        });
    };
})(jQuery);