import json
import random

locations = ["centro","sul","norte","oeste"]
stations = ["Lagos", "Odivelas", "Aveiro", "Geres", "Beja"]

totalEvents = []
for i in range(1,random.randint(10,20)): 
    if(i == 1): temp = random.randint(-10, -1)
    else: temp = random.randint(-10, 48)
    totalEvents.append({
        "station": random.choice(stations),
        "location": random.choice(locations),
        "temperature": temp,
        "type": "red" if temp >=38 or temp <=0 else "green"
    })

data = {"events": totalEvents}

# print(data)

# write the data to a file
with open("data.json", "w") as f:
    json.dump(data, f, indent=4, separators=(",", ": "))


### Statistics

countStations = []
for station in stations: 
    eventsStation = [e for e in totalEvents if e["station"] == station]
    print(eventsStation)
    if(len(eventsStation) == 0): 
        continue

    countStations.append({station: {
        "count": len(eventsStation),
        "minTemp":  min([e["temperature"] for e in eventsStation]),
        "maxTemp":  max([e["temperature"] for e in eventsStation]),
        "redAlertMinTemp":  min([e["temperature"] for e in eventsStation if e["type"] == "red"]) if len([e["temperature"] for e in eventsStation if e["type"] == "red"]) else None,
        "avgTemp": sum([e["temperature"] for e in eventsStation]) / len(eventsStation)
    }})




print(countStations)

countLocations = []
for location in locations:
    eventsLocation = [e for e in totalEvents if e["location"] == location]
    if(len(eventsLocation) == 0): 
        continue

    countLocations.append({location:{
        "count": len(eventsLocation),
        "minTemp":  min([e["temperature"] for e in eventsLocation]),
        "maxTemp":  max([e["temperature"] for e in eventsLocation]),
        "avgTemp": sum([e["temperature"] for e in eventsLocation]) / len(eventsLocation)
    }})

print(countLocations)

minTemp =  min([e["temperature"] for e in totalEvents])
maxTemp =  max([e["temperature"] for e in totalEvents])

countType = []
for type in ['green', 'red']: 
    countType.append({type: sum([1 for e in totalEvents if e["type"] == type] or 0) })

with open("data-stats.json", "w") as f:
    json.dump(countStations, f, indent=4, separators=(",", ": "))
    json.dump(countLocations, f, indent=4, separators=(",", ": "))
    json.dump(countType, f, indent=4, separators=(",", ": "))