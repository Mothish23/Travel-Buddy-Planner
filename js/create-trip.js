const tripForm = document.getElementById("tripForm");

if (tripForm) {
    tripForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const trip = {
            tripName: document.getElementById("tripName").value,
            destination: document.getElementById("destination").value,
            startDate: document.getElementById("startDate").value,
            endDate: document.getElementById("endDate").value,
            budget: Number(document.getElementById("budget").value),
            imageUrl: document.getElementById("imageUrl").value,
            description: document.getElementById("description").value
        };

        try {
            const savedTrip = await apiFetch("/trips", {
                method: "POST",
                body: JSON.stringify(trip)
            });

            window.location.href = `trip-details.html?id=${savedTrip.id}`;

        } catch (error) {
            document.getElementById("message").textContent = error.message;
        }
    });
}