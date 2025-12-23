document.addEventListener('DOMContentLoaded', () => {
    const buttons = document.querySelectorAll('.info-button');
    const contents = document.querySelectorAll('.item-content');

    buttons.forEach(button => {
        button.addEventListener('click', () => {
            buttons.forEach(b => b.classList.remove('active'));
            contents.forEach(c => c.classList.remove('active'));

            button.classList.add('active');

            const targetId = button.dataset.tab;
            const target = document.getElementById(targetId);

            if (target) {
                target.classList.add('active');
            }

        });
    });
});


function addToCart(button) {

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const slug = button.getAttribute("data-slug");
    fetch(`/books/book_info/${slug}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken

        },
        body: JSON.stringify({
            slug: slug, quantity: 1
        })
    })

        .then(res => res.json())
        .then(data => {
            if (data.success) {
                document.getElementById("cart-count").textContent = data.totalQuantity;
            } else {
                alert("Fehler!")
            }
        });
}


document.getElementById('feedbackForm').addEventListener('submit', function (e) {
    e.preventDefault();

    let formData = new FormData(this);
    let params = new URLSearchParams(formData);
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch('/books/feedback', {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken
        },
        body: params
    }).then(response => {
        if (response.ok) {

            let successDiv = document.getElementById('feedbackSuccess');
            successDiv.style.display = 'block';
            setTimeout(() => {
                successDiv.style.display = 'none';
                document.getElementById('feedbackModal').querySelector('form').reset();
                bootstrap.Modal.getInstance(document.getElementById('feedbackModal')).hide();
            }, 1000);
        } else {
            let failedDiv = document.getElementById('feedbackFailed');
            failedDiv.style.display = 'block';
        }
    });
});


document.querySelectorAll(".star-rating .fa-star").forEach((star) => {
    star.addEventListener("click", function () {
        let value = this.getAttribute("data-value");

        document.querySelectorAll(".star-rating .fa-star").forEach(s => {
            s.classList.remove("filled");
            if (s.getAttribute("data-value") <= value) {
                s.classList.add("filled");
            }
        });

        document.getElementById("ratingValue").value = value;
    });
});


document.querySelectorAll(".thumb-up, .thumb-down").forEach(btn => {
    btn.addEventListener("click", function () {
        const id = this.getAttribute("data-id");
        const like = this.classList.contains("thumb-up");

        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        fetch(`/books/feedback/${id}/vote?like=${like}`, {
            method: "POST",
            headers: {[csrfHeader]: csrfToken}
        })
            .then(res => res.json())
            .then(data => {
                this.parentNode.querySelector(".likes-number").textContent = data.likes;
                this.parentNode.querySelector(".dislikes-number").textContent = data.dislikes;
            })
            .catch(err => console.error(err));
    });
});