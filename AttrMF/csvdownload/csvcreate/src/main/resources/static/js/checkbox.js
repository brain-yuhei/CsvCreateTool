
// 全選択のチェックボックスに応じて、すべてのチェックボックスをON/OFF
function selectAllCheckboxes(source) {
    const checkboxes = document.querySelectorAll('input[type="checkbox"][name$=".checked"]');

    checkboxes.forEach(function(checkbox) {
        checkbox.checked = source.checked;
    });
}