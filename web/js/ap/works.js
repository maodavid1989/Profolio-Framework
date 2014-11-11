$( document ).ready(function() {
    $.ajax({
        async : false,
        url : "WorksServlet",
        dataType : 'json',
        data : {
            ajaxAction : "ajaxAction"
        },
        success : function(data, textStatus, xhr) {
            console.log(data.formData[0]);
        }
    });
    
});
function StockReport(){
     window.open('\Works_StockServlet?printName=CDStock');

}