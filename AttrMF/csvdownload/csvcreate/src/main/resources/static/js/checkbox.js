
// 全選択のチェックボックスに応じて、すべてのチェックボックスをON/OFF
function selectAllCheckboxes(source) {
    const checkboxes = document.querySelectorAll('input[type="checkbox"][name$=".checked"]');

    checkboxes.forEach(function(checkbox) {
        checkbox.checked = source.checked;
    });
}

function updateRowBackground(checkbox) {
    const row = checkbox.closest('tr');
    if (checkbox.checked) {
        row.classList.add('checked-row');
        row.classList.remove('unchecked-row');
    } else {
        row.classList.remove('checked-row');
        row.classList.add('unchecked-row');
    }
}

function initializeRowBackgrounds() {
    const checkboxes = document.querySelectorAll('input[type="checkbox"][name^="koutsuuhiList"][name$=".checked"]');
    checkboxes.forEach(cb => updateRowBackground(cb));
}

window.addEventListener('DOMContentLoaded', initializeRowBackgrounds);

/**
 * 全選択チェック処理（登録経路編集画面）
 * 
 * @param {*} source 
 */
function allCheckboxes(source) {
    const checkboxes = document.querySelectorAll('input[type="checkbox"][name$=".selected"]');

    checkboxes.forEach(function(checkbox) {
        checkbox.checked = source.checked;
    });
}