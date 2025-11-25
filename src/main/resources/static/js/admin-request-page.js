document.querySelectorAll(".reply1").forEach(btn => {

    btn.addEventListener("click", function() {
        document.getElementById("requestId").value = this.dataset.id;
        document.getElementById("requestType").textContent = this.dataset.type;
        document.getElementById("requestReason").textContent = this.dataset.reason;
        document.getElementById("requestStatus").textContent = this.dataset.status;

        const modal = new bootstrap.Modal(document.getElementById("adminReply"));

        modal.show();

    });
});

document.querySelectorAll(".status-text").forEach(st => {

    if (st.textContent.trim() === "NEW") {
        st.style.color = "red";
    } else if (st.textContent.trim() === "IN_PROGRESS") {
        st.style.color = "blue";
    } else if (st.textContent.trim() === "DONE") {
        st.style.color = "green";
    } else if (st.textContent.trim() === "REJECTED") {
        st.style.color = "orange";
    }
});

document.querySelectorAll('.btn-success').forEach(btn => {
    btn.addEventListener('click', function() {
        const form = document.getElementById('replyForm');
        const requestId =  document.getElementById("requestId").value;
        form.action = `/admin/user-requests/${requestId}/status`;
        document.getElementById("replyForm").submit();
    });
});