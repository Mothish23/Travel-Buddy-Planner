if (!isLoggedIn()) {
    window.location.href = "login.html";
}

const params = new URLSearchParams(window.location.search);
const tripId = params.get("id");

const editTripForm = document.getElementById("editTripForm");
const message = document.getElementById("message");

if (!tripId) {
    message.textContent = "Trip ID is missing.";
    editTripForm.style.display = "none";
} else {
    loadTripForEdit();
}

async function loadTripForEdit() {
    try {
        const trip = await apiFetch(`/trips/${tripId}`);

        document.getElementById("tripName").value = trip.tripName || "";
        document.getElementById("destination").value = trip.destination || "";
        document.getElementById("startDate").value = trip.startDate || "";
        document.getElementById("endDate").value = trip.endDate || "";
        document.getElementById("budget").value = trip.budget ?? "";
        document.getElementById("imageUrl").value = trip.imageUrl || "";
        document.getElementById("description").value = trip.description || "";

    } catch (error) {
        message.textContent = error.message;
    }
}

editTripForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    message.textContent = "";

    const updatedTrip = {
        tripName: document.getElementById("tripName").value.trim(),
        destination: document.getElementById("destination").value.trim(),
        startDate: document.getElementById("startDate").value,
        endDate: document.getElementById("endDate").value,
        budget: Number(document.getElementById("budget").value),
        imageUrl: document.getElementById("imageUrl").value.trim(),
        description: document.getElementById("description").value.trim()
    };

    if (updatedTrip.endDate < updatedTrip.startDate) {
        message.textContent = "End date cannot be before start date.";
        return;
    }

    try {
        await apiFetch(`/trips/${tripId}`, {
            method: "PUT", body: JSON.stringify(updatedTrip)
        });

        window.location.href = `trip-details.html?id=${tripId}`;

    } catch (error) {
        message.textContent = error.message;
    }
});
