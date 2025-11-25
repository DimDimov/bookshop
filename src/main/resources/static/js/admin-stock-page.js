document.querySelectorAll(".status-text").forEach(st => {

    if (st.textContent.trim() === "NEW") {
        st.style.color = "red";
    } else if (st.textContent.trim() === "SOLD") {
        st.style.color = "blue";
    } else if (st.textContent.trim() === "RETURNED") {
        st.style.color = "green";
    } else if (st.textContent.trim() === "DAMAGED") {
        st.style.color = "orange";
    }
});

document.addEventListener("DOMContentLoaded", function () {

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    document.querySelectorAll(".delete-stock-book-btn").forEach(btn => {
        btn.addEventListener("click", function () {
            const id = this.getAttribute("data-id");

            fetch(`/admin/stock/delete/${id}`, {
                method: 'DELETE',
                headers: {
                    [csrfHeader]: csrfToken
                }
            })
                .then(response => {
                    if(response.ok) {
                        location.reload();
                    }
                    else {
                        alert("Fehler!")
                    }
                })
        });
    });
});