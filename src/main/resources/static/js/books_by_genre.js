function addToCart(bookId) {

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    fetch('/cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken

        },
        body: JSON.stringify({bookId: bookId, quantity: 1
        })
    })

        .then(res => res.json())
        .then(data => {
            if(data.success) {
                document.getElementById("cart-count").textContent = data.totalQuantity;
            } else {
                alert("Fehler!")
            }
        });
}

function updateCartCount() {
    fetch('/cart/total')
        .then(response =>
            response.text())
        .then(total => {
            document.querySelector('.cart-count').textContent = total;
        })
        .catch(error =>
            console.error('Waren nicht', error)
        )
}