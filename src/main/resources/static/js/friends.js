document.addEventListener("click", function (event) {
  // Hide the tooltip when clicking outside the trigger element
  if (event.target !== triggerElement) {
    tooltipContainer.style.display = "none";
  }
});
function makeEditable(fieldName) {
  const element = document.querySelector(`.${fieldName}`);
  const currentValue = element.innerText;

  // Create an input element
  const inputElement = document.createElement("input");
  inputElement.value = currentValue;

  // Set the input element's class and focus
  inputElement.className = "form-control";
  inputElement.addEventListener("blur", () =>
    updateValue(fieldName, inputElement.value)
  );
  inputElement.addEventListener("keyup", (event) => {
    if (event.key === "Enter") {
      inputElement.blur();
    }
  });

  // Replace the span with the input
  element.replaceWith(inputElement);

  // Focus the input
  inputElement.focus();
}

function updateValue(fieldName, newValue) {
  // Create a new span element
  const spanElement = document.createElement("span");
  spanElement.className = `editable ${fieldName}`;
  spanElement.innerText = newValue;

  // Replace the input with the span
  document.querySelector(`.${fieldName}`).replaceWith(spanElement);
}
