/**
 * Created by zhangqiang13 on 2016/8/10.
 */

$(document).ready(function() {
    $(".edit-posts").click(function () {
        $(this).parent().next().toggle(400);
    });

    $(".add-sentence-btn").click(function () {
        $(this).toggle(100);
        $(this).next().toggle(400);
    });

    $(".edit-title").on("input", function(){
         $(this).parents(".panel-body").first().find("h3").first().html($(this).val());
    });

    //$(".copy-sentence").click(function (){
    //    var sentence_text = $(this).parent().parent().find(".show-sentence").innerHTML;
    //    window.prompt("Copy to clipboard: Ctrl+C, Enter", sentence_text);
    //});
    $(".copy-sentence").mouseleave(function (e){
        e.currentTarget.setAttribute('class', 'copy-sentence');
        e.currentTarget.innerHTML = "Copy";
    });

});

window.onload = function() {
    //your script here

    var clipboard = new Clipboard('.copy-sentence');

    clipboard.on('success', function(e) {
        console.info('Action:', e.action);
        console.info('Text:', e.text);
        console.info('Trigger:', e.trigger);

        e.clearSelection();

        showTooltip(e.trigger,'Copied!');
    });

    clipboard.on('error', function(e) {
        console.error('Action:', e.action);
        console.error('Trigger:', e.trigger);

        showTooltip(e.trigger,fallbackMessage(e.action));
    });
}

function validate_required(field)
{
    with (field)
    {
        if (value==null||value==""){
            //alert(alerttxt);
            $(field).parent().prev().toggle(400);
            return false
        }else {
            return true
        }
    }
}

function validate_form(thisform)
{
    with (thisform)
    {
        if (!validate_required(sentence)){
            sentence.focus();
            return false
        }
    }
}

function showTooltip(elem, msg) {
    elem.setAttribute('class', 'copy-sentence tip');
    elem.setAttribute('aria-label', msg);
    elem.innerHTML = "Copied";
}
function fallbackMessage(action) {
    var actionMsg = '';
    var actionKey = (action === 'cut' ? 'X' : 'C');
    if (/iPhone|iPad/i.test(navigator.userAgent)) {
        actionMsg = 'No support :(';
    }
    else if (/Mac/i.test(navigator.userAgent)) {
        actionMsg = 'Press ?-' + actionKey + ' to ' + action;
    }
    else {
        actionMsg = 'Press Ctrl-' + actionKey + ' to ' + action;
    }
    return actionMsg;
}

