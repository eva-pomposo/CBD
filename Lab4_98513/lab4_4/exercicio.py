from neo4j import GraphDatabase
from os import write

driver = GraphDatabase.driver("bolt://localhost:7687", auth=("neo4j", "qweasd"))

driver.session().run(
    "LOAD CSV WITH HEADERS FROM 'file:///Car_Prices_Poland_Kaggle.csv' AS row MERGE (vehicle:Vehicle { num: toInteger(row.num), year:toInteger(row.year), mileage:toInteger(row.mileage),  vol_engine:toInteger(row.vol_engine), price:toInteger(row.price) }) MERGE (fuel:Fuel { name: row.fuel})  MERGE (province:Province { name: row.province})  MERGE (mark:Mark { name: row.mark, model: row.model})  MERGE (vehicle)-[:TYPE_OF]->(fuel)  MERGE (vehicle)-[:IS_A {generation_name: row.generation_name}]->(mark)  MERGE (vehicle)-[:FROM {city: row.city}]->(province);"
)

file = open("CBD_L44c_output.txt", "a")

file.write("query1 - Liste a informação de cada veiculo\n\n")
file.write("match (vehicle:Vehicle) return vehicle\n\n")
resultes = list(driver.session().run("match (vehicle:Vehicle) return vehicle"))
for res in resultes:
    file.write(str(res) + "\n")
file.write(
    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
)

file.write("query2 - Liste os veiculos com o combustivel 'Diesel'\n\n")
file.write(
    "match (vehicle)-[:TYPE_OF]->(fuel:Fuel {name:'Diesel'}) return vehicle.num\n\n"
)
resultes = list(
    driver.session().run(
        "match (vehicle)-[:TYPE_OF]->(fuel:Fuel {name:'Diesel'}) return vehicle.num"
    )
)
for res in resultes:
    file.write(str(res) + "\n")
file.write(
    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
)

file.write(
    "query3 - Liste marcas e total de modelos em cada marca. Ordene por o numero de modelos.\n\n"
)
file.write(
    "match (mark:Mark) return mark.name as mark, count(mark.model) as totalModel order by totalModel;\n\n"
)
resultes = list(
    driver.session().run(
        "match (mark:Mark) return mark.name as mark, count(mark.model) as totalModel order by totalModel"
    )
)
for res in resultes:
    file.write(str(res) + "\n")
file.write(
    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
)

file.write(
    "query4 - Liste os veiculos da province 'Mazowieckie', mas que nao sao da cidade 'Janki'. \n\n"
)
file.write(
    "match (vehicle)-[rel:FROM]->(province:Province {name: 'Mazowieckie'}) where rel.city <> 'Janki' return vehicle.num as numVehicle, rel.city as city\n\n"
)
resultes = list(
    driver.session().run(
        "match (vehicle)-[rel:FROM]->(province:Province {name: 'Mazowieckie'}) where rel.city <> 'Janki' return vehicle.num as numVehicle, rel.city as city"
    )
)
for res in resultes:
    file.write(str(res) + "\n")
file.write(
    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
)

file.write(
    "query5 - Encontre os veiculos com a marca 'toyota' e com a generation_name 'gen-ii-2003-2009', ou que usam o combustivel 'Gasoline'. \n\n"
)
file.write(
    "call { match (vehicle)-[:IS_A {generation_name:'gen-ii-2003-2009'}]->(mark:Mark {name: 'toyota'}) return vehicle union match (vehicle)-[:TYPE_OF]-(fuel:Fuel {name:'Gasoline'}) return vehicle } return vehicle.num as numVehicle\n\n"
)
resultes = list(
    driver.session().run(
        "call { match (vehicle)-[:IS_A {generation_name:'gen-ii-2003-2009'}]->(mark:Mark {name: 'toyota'}) return vehicle union match (vehicle)-[:TYPE_OF]-(fuel:Fuel {name:'Gasoline'}) return vehicle } return vehicle.num as numVehicle"
    )
)
for res in resultes:
    file.write(str(res) + "\n")
file.write(
    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
)

file.write(
    "query6 - Liste para cada tipo de combustivel, o preco total de veiculos com esses combustiveis. \n\n"
)
file.write(
    "match (vehicle)-[:TYPE_OF]->(fuel) return fuel.name as fuel, sum(vehicle.price) as totalPrice\n\n"
)
resultes = list(
    driver.session().run(
        "match (vehicle)-[:TYPE_OF]->(fuel) return fuel.name as fuel, sum(vehicle.price) as totalPrice"
    )
)
for res in resultes:
    file.write(str(res) + "\n")
file.write(
    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
)

file.write(
    "query7 - Liste para cada marca e modelo o ano do veiculo mais recente. \n\n"
)
file.write(
    "match (vehicle)-[:IS_A]->(mark) return mark.name as mark, mark.model as model, max(vehicle.year) as year\n\n"
)
resultes = list(
    driver.session().run(
        "match (vehicle)-[:IS_A]->(mark) return mark.name as mark, mark.model as model, max(vehicle.year) as year"
    )
)
for res in resultes:
    file.write(str(res) + "\n")
file.write(
    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
)

file.write("query8 - Para cada vol_engine determine a media de mileage. \n\n")
file.write(
    "match (vehicle:Vehicle) return vehicle.vol_engine as vol_engine, avg(vehicle.mileage) as avgMileage\n\n"
)
resultes = list(
    driver.session().run(
        "match (vehicle:Vehicle) return vehicle.vol_engine as vol_engine, avg(vehicle.mileage) as avgMileage"
    )
)
for res in resultes:
    file.write(str(res) + "\n")
file.write(
    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
)

file.write(
    "query9 - Recomende 15 veiculos para quem gosta do veiculo numero '117171', com base na marca, ou com base no fuel e do preco com intervalos a variar de 10000 euros.Ordene por ordem do preco. \n\n"
)
file.write(
    "call { match (vehicle1:Vehicle {num: 117171})-[:IS_A]->(marca) match (vehicle2:Vehicle)-[:IS_A]->(mark:Mark {name: marca.name }) where vehicle2.num <> 117171 return vehicle2 union match (vehicle1:Vehicle {num: 117171})-[:TYPE_OF]->(combustivel) match (vehicle2)-[:TYPE_OF]->(fuel:Fuel {name:combustivel.name}) where vehicle1.price + 10000 >= vehicle2.price and vehicle1.price - 10000 <= vehicle2.price and vehicle2.num <> 117171 return vehicle2  } return vehicle2.num as numVehicle, vehicle2.price as price order by price limit 15\n\n"
)
resultes = list(
    driver.session().run(
        "call { match (vehicle1:Vehicle {num: 117171})-[:IS_A]->(marca) match (vehicle2:Vehicle)-[:IS_A]->(mark:Mark {name: marca.name }) where vehicle2.num <> 117171 return vehicle2 union match (vehicle1:Vehicle {num: 117171})-[:TYPE_OF]->(combustivel) match (vehicle2)-[:TYPE_OF]->(fuel:Fuel {name:combustivel.name}) where vehicle1.price + 10000 >= vehicle2.price and vehicle1.price - 10000 <= vehicle2.price and vehicle2.num <> 117171 return vehicle2  } return vehicle2.num as numVehicle, vehicle2.price as price order by price limit 15"
    )
)
for res in resultes:
    file.write(str(res) + "\n")
file.write(
    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
)

file.write(
    "query10 - Liste os veiculos com a marca a comecar por 'v' e com o ano superior a 2015.\n\n"
)
file.write(
    "match (vehicle)-[:IS_A]->(mark) where mark.name starts with 'v' and vehicle.year > 2015 return vehicle.num as numVehicle, mark.name as mark, vehicle.year as year\n\n"
)
resultes = list(
    driver.session().run(
        "match (vehicle)-[:IS_A]->(mark) where mark.name starts with 'v' and vehicle.year > 2015 return vehicle.num as numVehicle, mark.name as mark, vehicle.year as year"
    )
)
for res in resultes:
    file.write(str(res) + "\n")

file.close()
driver.close()
