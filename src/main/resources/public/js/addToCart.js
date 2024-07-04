/*<![CDATA[*/
$(document).ready(function () {
        $('.item-add-to-cart-button')
            .on('click', function(event) {
                event.stopPropagation();
                event.preventDefault();

                const productId = $(this).attr('data-product-id');
                const productType = $(this).attr('data-product-type');
                const csrfToken = $('meta[name="_csrf"]').attr('content');
                const successMessage = $(this).attr('data-success-message');
                const failMessage = $(this).attr('data-fail-message');

                const data = {
                        productId: productId,
                        productType: productType
                };

                $.ajax({
                        url: '/product/add-to-cart',
                        type: 'POST',
                        data: JSON.stringify(data),
                        contentType: 'application/json',
                        beforeSend: function(xhr) {
                                xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken);
                        },
                        success: function (result, status, xhr) {
                                if (xhr.getResponseHeader("Content-Type").startsWith("text/html")) {
                                        // If the response is an HTML document (probably the login page), redirect to login
                                        window.location.href = "/login";
                                } else {
                                        showNotification(successMessage, 'rgba(0, 128, 0, 0.8)');
                                }

                                // Manually increment numItems
                                var numItemsDisplay = document.querySelector('.cart-item-count');  // Replace with an actual selector
                                numItemsDisplay.textContent = parseInt(numItemsDisplay.textContent) + 1;
                        },
                        error: function () {
                                showNotification(failMessage, 'rgba(228, 0, 0, 0.8)');
                        }
                });
        });
});
/*]]>*/

function showNotification(message, bgColor) {
        var $notification = $('<div class="notification"></div>')
            .text(message)
            .css('background-color', bgColor);

        $('body')
            .append($notification);

        setTimeout(function () {
                $notification.fadeOut("slow", function () {
                        $(this).remove();
                });
        }, 3000);
}