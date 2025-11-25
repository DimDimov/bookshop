document.addEventListener("DOMContentLoaded", function () {


    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    document.querySelectorAll(".delete-btn").forEach(btn => {

        btn.addEventListener("click", function () {
            const id = this.getAttribute("data-id");

            fetch(`/delivery/delete_delivery/${id}`, {
                method: 'DELETE',
                headers: {
                    [csrfHeader]: csrfToken
                }
            })
                .then(response => {
                    if (response.ok) {
                        document.getElementById(`delivery-${id}`).remove();
                    } else {
                        alert("Fehler beim Löschen.")
                    }
                });
        });
    });
});
