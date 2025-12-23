
window.i18n = {
    books: '[[#{books}]]'
};

function updateQuantity(bookId, change) {

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch("/cart/update", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({bookId: bookId, changeQuantity: change})
    })
        .then(res => res.json())
        .then(data => {

            if (data.success) {

                document.getElementById("quantity-" + bookId).textContent = data.newQuantity;

                document.querySelector(".cart-count").textContent = data.totalQuantity;

                document.getElementById("price1-" + bookId).textContent = data.itemTotalPrice.toFixed(2) + ' €';

                document.getElementById("totalSum").textContent = data.totalCartPrice.toFixed(2) + '  €';

                if(data.totalQuantity === 0) {
                    document.getElementById("checkout-block").style.display = 'none';

                    document.getElementById("empty-block").style.display = 'table-cell';

                    document.getElementById("bookCount").innerText = "0";
                }

            } else {
                alert("Fehler!");
            }
        });
}

function removeBookFromCart(bookId) {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch("/cart/delete_item/" + bookId, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            [csrfHeader]: csrfToken
        }
    })
        .then(res => res.json())
        .then(data => {
            if(data.success) {

                const  row = document.getElementById("cart-item-" + bookId);
                if(row) row.remove();

                if(data.totalQuantity !== undefined) {
                    document.querySelector(".cart-count").textContent = data.totalQuantity;
                }

                if(data.totalPrice !== undefined) {
                    document.querySelector(".price1").textContent = data.totalPrice;
                }

                if(data.totalCartPrice !== undefined) {
                    document.getElementById("totalSum").textContent = data.totalCartPrice.toFixed(2) + '  €';
                }
            } else {
                alert("Fehler beim Entfernen Buches");
            }
        });
}