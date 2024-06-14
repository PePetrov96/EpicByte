$(document).ready(function () {
    $('.item-add-to-cart-button').on('click', function(event) {
        event.preventDefault();
        event.stopPropagation();
        event.stopImmediatePropagation();

        var productId = $(this).attr('data-product-id');
        var productType = $(this).attr('data-product-type');

        $.ajax({
            url: '/product/add-to-cart',
            type: 'POST',
            data: {
                productId: productId,
                productType: productType
            },
            success: function() {
                alert('Product added to cart successfully.');
            },
            error: function() {
                alert('Failed to add product to cart. Please try again.');
            }
        });
    });
});