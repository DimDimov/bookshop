document.getElementById("searchForm").addEventListener("submit", function(e) {
    e.preventDefault();
    const query = document.getElementById("searchQuery").value;

    fetch(`/search?query=${encodeURIComponent(query)}`)
        .then(response => response.json())
        .then(books => {
            const list = document.getElementById("resultList");
            list.innerHTML = "";

            if(books.length === 0) {
                list.innerHTML = "<li class = 'list-group-item'>Kein Ergebnis gefunden</li>"
            } else {
                books.forEach(book => {
                    const li = document.createElement("li");
                    li.classList.add("list-group-item");
                    li.innerHTML = `<a href = "/books/book_info/${book.slug}">${book.title}</a>`;
                    list.appendChild(li);
                });
            }

            new bootstrap.Modal(document.getElementById("searchResultModal")).show();
        });
});