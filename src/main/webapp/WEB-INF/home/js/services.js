/**
* Simulates a web service yielding persons
@author loricarlos@gmail.com
@version demo

*/

const Gender = {
    MALE: 2,
    FEMALE: 4
}

class Person {
    static #id_counter = 0
    #id
    #firstname;
    #lastname;
    #age;
    #gender;
    constructor(firstname, lastname, age, gender) {
        [this.#firstname, this.#lastname, this.#age, this.#gender] =
            [firstname, lastname, age, gender]
        this.#id = Person.#id_counter++
    }
    get id() { return this.#id }
    get firstname() { return this.#firstname }
    get lastname() { return this.#lastname }
    get age() { return this.#age }
    get gender() { return this.#gender }
    toObj() {
        return {
            id: this.id,
            firstname: this.firstname,
            lastname: this.lastname,
            age: this.age,
            gender: this.gender == Gender.MALE ? "M" : "F"
        }
    }
}


export function get_persons(url = "/person", delay = 3) {
    return fetch('/persons')
        .then(response => response.ok ? response.json() : null)
        .catch(err => console.log(err))
}


// TRUE Function
const True = person => true

//Combinator of functions
const apply_filters = (queryOptions, ...filters) => filters.reduce(
    (prev_filters, current_filter) => person =>
        prev_filters(person, queryOptions) && current_filter(person, queryOptions), True)

// Gender - filter
const gender_filter = (person, { gender }) => gender ? person.gender === gender : true

// Age - filter
const age_filter = (person, { ageRange }) =>
    ageRange ? person.age >= ageRange.min && person.age <= ageRange.max : true


// export const getPersonsBySelection = (URI = "/person", queryOptions, delay = 3) =>
//     fetch(`${URI}`)
//         .then(response => response.ok ? response.json() : null)
//         .then(persons => persons.filter(apply_filters(queryOptions, gender_filter, age_filter)))
//         .catch(err => console.log(err))

export const getPersonsBySelection = (URI = "/persons", queryOptions, delay = 3) =>
    fetch(`${URI}?agemin=${queryOptions.ageRange.min}&agemax=${queryOptions.ageRange.max}`)
        .then(response => response.ok ? response.json() : null)
        .then(persons => persons.filter(person => gender_filter(person, queryOptions)))
        .catch(err => console.log(err))


export const getPersonById = (URI = "/persons", id) =>
    fetch(`${URI}?id=${id}`)
        .then(response => response.ok ? response.json() : null)
        .then(person => [person])

        .catch(err => console.log(err))