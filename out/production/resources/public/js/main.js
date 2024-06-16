'use strict'

// OLD PAGE PADDING ADDING LOGIC
// document.addEventListener("DOMContentLoaded", adjustBodyPadding);
// window.addEventListener("resize", adjustBodyPadding);
//
// function adjustBodyPadding() {
//     const headerHeight = document.querySelector("header").offsetHeight;
//     document.body.style.paddingTop = headerHeight + "px";
// }

// Fetch all the forms we want to apply custom Bootstrap validation styles to
const forms = document.querySelectorAll('.needs-validation')
// Loop over them and prevent submission
Array.from(forms).forEach(form => {
    form.addEventListener('submit', event => {
        if (!form.checkValidity()) {
            event.preventDefault()
            event.stopPropagation()
        }
        form.classList.add('was-validated')
    }, false)
});

function topFunction() {
    document.body.scrollTop = 0; // For Safari
    document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
}

document.addEventListener('DOMContentLoaded', function () {
    // Get the modal
    let modal = document.getElementById("imageModal");
    // Get the button that closes the modal
    let closeButton = document.querySelector('.btn-close');
    // Get the image and insert it inside the modal
    let img = document.querySelector(".product-details-image-container img");
    let modalImg = document.getElementById("modalImage");

    // If these elements exist on the page, then add the event listeners
    if (modal && closeButton && img && modalImg)  {
        img.onclick = function () {
            modal.style.display = "block";
            modalImg.src = this.src;
            modalImg.style.height = "80vh";
            modalImg.style.objectFit = "cover";
        }

        closeButton.onclick = function () {
            modal.style.display = "none";
        }

        window.onclick = function(event) {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        }
    }
}, false);

// // Add SockJS and Stomp logic to end of your existing JavaScript
// document.addEventListener("DOMContentLoaded", function() {
//     // Connect to WebSocket and subscribe to "/cart/size"
//     var socket = new SockJS("/ws");
//     var stompClient = Stomp.over(socket);
//     stompClient.connect({}, function(frame) {
//         stompClient.subscribe("/cart/size", function(size) {
//             var cartSize = JSON.parse(size.body);
//
//             $(".cart-item-count").text(cartSize);
//         });
//     });
// });