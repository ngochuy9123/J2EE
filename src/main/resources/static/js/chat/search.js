const searchDiv = document.getElementById("search")

searchDiv.addEventListener("keyup", async (e) => {
    const value = e.target.value
    const resp = await fetch("/api/findRooms/"+value)
    const data = await resp.json()

    setupSidePanel(data)
})
