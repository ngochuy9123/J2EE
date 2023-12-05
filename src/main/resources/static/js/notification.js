const generateAnnouncementHtml = (announcement) => {

    const innerSpan = announcement['eAnnounceType'] === "LIKE"
        ? "đã like bài viết của bạn"
        : "đã bình luận trong bài viết của bạn"

    return `
    <li onclick="seeMorePost(${announcement['idPost']})">
        <a class="dropdown-item" href="#" >

            <i class="user">
                <img
                    src="${announcement['avatar']}"
                    alt=""
                />
                <span class="detail-noti">  <strong>
                    <span>${announcement['username']}</span>
                    </strong>
                       <span>${innerSpan}</span>
                     </span>

            </i>
        </a></li>
    `
}


const generateBellTotalAnnouncement = (total) => {
    return total > 0 ?
        `<div class="notice-notifi">${total}</div>`
        : ""



}

// Notification
const handleNotification = async () => {
    const resp = await fetch("/api/notification")
    const data = await resp.json();
    const html = data.map(generateAnnouncementHtml).join()

    const announceItemsDiv = document.getElementById("announceItems")
    announceItemsDiv.innerHTML = html

    const announceTotal = document.getElementById("bellIcon")
    announceTotal.innerHTML = generateBellTotalAnnouncement(data.length)

}

async function changeStatusNotification(userId) {

    let data = new FormData
    data.append("userId", userId)

    const resp = await fetch("/changeStatusAnnounce",
        {
            method: "POST",
            body: data
        })
    await resp.text();

    const announceTotal = document.getElementById("bellIcon")
    console.log("a")
    announceTotal.innerHTML = ""

}