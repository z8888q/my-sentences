/**
 * Created by zhangqiang13 on 2016/8/10.
 */

$(document).ready(function() {
    $(".edit-posts").click(function () {
        $(this).parent().next().toggle(400);
    });

    $(".edit-title").on("input", function(){
         $(this).parents(".panel-body").first().find("h3").first().html($(this).val());
    });
});