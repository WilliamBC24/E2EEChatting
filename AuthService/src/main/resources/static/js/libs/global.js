/* DEVELOPMENT ONLY */
$(document).ready(function(){
    $('#fill-reg').click(e => {
        $('#username').val('ascii1102');
        $('#password').val('lamdepdai1');
        $('#re-password').val('lamdepdai');
        $('#email').val('ascii1102@gmail.com');
        $('#agree-to-term').prop('checked', true);
    });

    $('#fill-admin').click(e => {
        $('#username').val('ascii1102');
        $('#password').val('lamdepdai1');
    })

    $('#fill-user').click(e => {
        $('#username').val('ascii1102_usr');
        $('#password').val('lamdepdai1');
    });
})
/* DEVELOPMENT ONLY */

/* Toaster configuration */
$(document).ready(() => {
    showToast('error-toaster');
    showToast('success-toaster');
    showToast('message-toaster');
    showToast('info-toaster');
    let tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    })
});

$(document).ready(() => {

})

function showToast(eId) {
    const toastElement = document.getElementById(eId);
    if(toastElement !== null) {
        let toast = bootstrap.Toast.getOrCreateInstance(toastElement);
        toast.show();
    }
}
/* Toaster configuration */

/* Http AJAX */
function ajaxCall(url, method, data) {
    $.ajax({
        url: url,
        type: method,
        success: function (result) {
            // Xử lý kết quả sau khi xóa
            $('#confirmDeleteModal').modal('hide');
        },
        error: function (xhr, status, error) {
            // Xử lý lỗi nếu có
        }
    });
}
/* Http AJAX */