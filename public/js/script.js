const btnModule = document.getElementById("btnModule");
const modal = document.querySelector(".modalModul");
const annuler = document.querySelector(".annuler");


btnModule.addEventListener('click', () => {
    modal.classList.add("afficheModal");

    // alert("azerty");
})


annuler.addEventListener('click', () => {
    // alert("merci");

    modal.classList.add("fermerModal");

    // alert("azerty");
})