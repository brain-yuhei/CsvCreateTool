function submitDelete(id) {
    if (confirm("本当に削除しますか？")) {
        document.getElementById("deleteId").value = id;
        document.getElementById("deleteForm").submit();
    }
}