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
});

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