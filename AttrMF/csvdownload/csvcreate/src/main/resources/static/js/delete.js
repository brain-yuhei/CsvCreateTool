function submitDelete(id) {
    if (confirm("本当に削除しますか？")) {
        document.getElementById("deleteId").value = id;
        document.getElementById("deleteForm").submit();
    }
}

document.addEventListener("DOMContentLoaded", function() {
    const deleteBtn = document.getElementById("deleteBtn");
    deleteBtn.addEventListener("click", function() {
        const checkbox = document.querySelectorAll('input[type="checkbox"][name$=".selected"]:checked');

        const checkDeleteForm = document.getElementById("checkDeleteForm");

        // 既存hidden要素をクリア
        checkDeleteForm.innerHTML = "";

        checkbox.forEach(cb => {
            const tr = cb.closest("tr");
            const idInput = tr.querySelector('input[type="hidden"][name$=".id"]');
            if (idInput) {
                const hidden = document.createElement("input");
                hidden.type = "hidden";
                hidden.name = "ids"; 
                hidden.value = idInput.value;
                checkDeleteForm.appendChild(hidden);
            }
        });
        checkDeleteForm.submit();
    });
});


