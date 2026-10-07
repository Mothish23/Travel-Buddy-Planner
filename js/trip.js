const params = new URLSearchParams(window.location.search);
const tripId = params.get("id");

const DEFAULT_TRIP_IMAGE = "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=80";

const DEFAULT_PLACE_IMAGE = "https://images.unsplash.com/photo-1500534623283-312aade485b7?auto=format&fit=crop&w=800&q=80";

function escapeHtml(value) {
    return String(value ?? "")
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

async function loadTrip() {
    if (!tripId) {
        alert("Trip ID is missing.");
        window.location.href = "dashboard.html";
        return;
    }

    try {
        const trip = await apiFetch(`/trips/${tripId}`);

        document.getElementById("tripName").textContent = trip.tripName || "";
        document.getElementById("destination").textContent = `📍 ${trip.destination || ""}`;
        document.getElementById("dates").textContent = `${trip.startDate || ""} → ${trip.endDate || ""}`;
        document.getElementById("budget").textContent = `₹${trip.budget ?? 0}`;
        document.getElementById("description").textContent = trip.description || "No description";

        const tripImage = document.getElementById("tripImage");
        tripImage.src = trip.imageUrl || DEFAULT_TRIP_IMAGE;
        tripImage.onerror = function () {
            this.onerror = null;
            this.src = DEFAULT_TRIP_IMAGE;
        };

        const editButton = document.getElementById("editTripBtn");
        if (editButton) {
            editButton.href = `edit-trip.html?id=${trip.id}`;
        }

        await loadPlaces();
        await loadActivities();
        await loadExpenses();

    } catch (error) {
        alert(error.message);
    }
}

async function loadPlaces() {
    try {
        const places = await apiFetch(`/trips/${tripId}/places`);
        const container = document.getElementById("placesList");

        container.innerHTML = "";

        if (!places || places.length === 0) {
            container.innerHTML = `
                <p class="empty-list">
                    No places added yet.
                </p>
            `;
            return;
        }

        places.forEach(place => {
            const card = document.createElement("div");
            card.className = "mini-card";

            const imageUrl = place.imageUrl || DEFAULT_PLACE_IMAGE;

            card.innerHTML = `
                <img
                    class="place-image"
                    src="${escapeHtml(imageUrl)}"
                    alt="${escapeHtml(place.name || "Place")}"
                    onerror="this.onerror=null; this.src='${DEFAULT_PLACE_IMAGE}';"
                >

                <div class="mini-card-content">
                    <h4>${escapeHtml(place.name || "")}</h4>

                    <p>
                        ${escapeHtml(place.description || "No description")}
                    </p>

                    <button
                        class="btn danger small"
                        onclick="deletePlace(${place.id})">
                        Delete
                    </button>
                </div>
            `;

            container.appendChild(card);
        });

    } catch (error) {
        alert(error.message);
    }
}

async function loadActivities() {
    try {
        const activities = await apiFetch(`/trips/${tripId}/activities`);
        const container = document.getElementById("activitiesList");

        container.innerHTML = "";

        if (!activities || activities.length === 0) {
            container.innerHTML = `
                <p class="empty-list">
                    No activities added yet.
                </p>
            `;
            return;
        }

        activities.forEach(activity => {
            container.innerHTML += `
                <div class="mini-card">
                    <h4>${escapeHtml(activity.name || "")}</h4>

                    <p>${escapeHtml(activity.activityDate || "")}</p>

                    <p>
                        ${escapeHtml(activity.description || "")}
                    </p>

                    <strong>₹${escapeHtml(activity.cost ?? 0)}</strong>

                    <br><br>

                    <button
                        class="btn danger small"
                        onclick="deleteActivity(${activity.id})">
                        Delete
                    </button>
                </div>
            `;
        });

    } catch (error) {
        alert(error.message);
    }
}

async function loadExpenses() {
    try {
        const expenses = await apiFetch(`/trips/${tripId}/expenses`);
        const container = document.getElementById("expensesList");

        container.innerHTML = "";

        if (!expenses || expenses.length === 0) {
            container.innerHTML = `
                <p class="empty-list">
                    No expenses added yet.
                </p>
            `;
            return;
        }

        expenses.forEach(expense => {
            container.innerHTML += `
                <div class="mini-card">
                    <h4>${escapeHtml(expense.title || "")}</h4>

                    <p>${escapeHtml(expense.category || "Other")}</p>

                    <strong>₹${escapeHtml(expense.amount ?? 0)}</strong>

                    <br><br>

                    <button
                        class="btn danger small"
                        onclick="deleteExpense(${expense.id})">
                        Delete
                    </button>
                </div>
            `;
        });

    } catch (error) {
        alert(error.message);
    }
}

async function deletePlace(id) {
    if (!confirm("Delete this place?")) {
        return;
    }

    try {
        await apiFetch(`/places/${id}`, {
            method: "DELETE"
        });

        await loadPlaces();

    } catch (error) {
        alert(error.message);
    }
}

async function deleteActivity(id) {
    if (!confirm("Delete this activity?")) {
        return;
    }

    try {
        await apiFetch(`/activities/${id}`, {
            method: "DELETE"
        });

        await loadActivities();

    } catch (error) {
        alert(error.message);
    }
}

async function deleteExpense(id) {
    if (!confirm("Delete this expense?")) {
        return;
    }

    try {
        await apiFetch(`/expenses/${id}`, {
            method: "DELETE"
        });

        await loadExpenses();

    } catch (error) {
        alert(error.message);
    }
}

const placeForm = document.getElementById("placeForm");

if (placeForm) {
    placeForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const place = {
            name: document.getElementById("placeName").value.trim(),
            description: document.getElementById("placeDescription").value.trim(),
            imageUrl: document.getElementById("placeImage").value.trim()
        };

        try {
            await apiFetch(`/trips/${tripId}/places`, {
                method: "POST", body: JSON.stringify(place)
            });

            placeForm.reset();
            await loadPlaces();

        } catch (error) {
            alert(error.message);
        }
    });
}

const activityForm = document.getElementById("activityForm");

if (activityForm) {
    activityForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const activity = {
            name: document.getElementById("activityName").value.trim(),
            activityDate: document.getElementById("activityDate").value,
            description: document.getElementById("activityDescription").value.trim(),
            cost: Number(document.getElementById("activityCost").value || 0)
        };

        try {
            await apiFetch(`/trips/${tripId}/activities`, {
                method: "POST", body: JSON.stringify(activity)
            });

            activityForm.reset();
            await loadActivities();

        } catch (error) {
            alert(error.message);
        }
    });
}

const expenseForm = document.getElementById("expenseForm");

if (expenseForm) {
    expenseForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const expense = {
            title: document.getElementById("expenseTitle").value.trim(),
            category: document.getElementById("expenseCategory").value.trim(),
            amount: Number(document.getElementById("expenseAmount").value),
            expenseDate: document.getElementById("expenseDate").value
        };

        try {
            await apiFetch(`/trips/${tripId}/expenses`, {
                method: "POST", body: JSON.stringify(expense)
            });

            expenseForm.reset();
            await loadExpenses();

        } catch (error) {
            alert(error.message);
        }
    });
}

loadTrip();
