
$( document ).ready(function() {
    $.ajax({
        async : false,
        url : "IndexServlet",
        dataType : 'json',
        data : {
            ajaxAction : "ajaxAction"
        },
        success : function(data, textStatus, xhr) {
            console.log(data.formData[0]);
        }
    });
    
});