# `JSON` - Standard Data Format

---

- another example of this is `.xml`

## Syntax:
- Supports the following data types:
  - numbers
  - strings
  - booleans
  - null
- Aggregate data types:
  - arrays `[]`
  - object `{}`

---

### JSON Example:
```json
{
  "Movies": [
    {
      "Title": "The Odyssey",
      "Director": "Christopher Nolan",
      "Rating": 10
    },
    {
      "Title": "Dune pt.III",
      "Director": "Denis Villeneuve",
      "Rating": null
    }
  ],
  "Shows": [
    {
      "Title": "Andor",
      "Show-Runner": "Tony Gilroy",
      "Rating": 10
    }
  ]
}
```

---

# Parsing JSON Data
- **Most languages provide JSON parsers, so there's no need to write your own**
- Three Major Types of Parsers:
  1. DOM Parsers
     - Convert JSON text to an in-memory tree data structure
     - After running the parser to create a DO, traverse the DOM to extract the data you want
  2. Stream Parsers
     - Tokenizers that return one token at a time from the JSON data file
  3. Serializers / Deserializers
     - Use a library to convert from JSON to Java Objects 
     - GSON and Jackson are both popular libraries 
- If you are parsing JSON in your own project, you should not create your own. You should use different libraries that do it for you. (i.e., GSON)
- `{` can be seen as a begin object token
- The next token would be the `Property Name`
- The next would be the `begin Array`
- ...
- The last would be the `}` (end token)
- JsonWriter:
  - Arguably the better library for JSON Parsing and the one we should use for this class along with GSON. 

---

# DOM Parser
- Higher-level parser than above methods
- Returns the whole file in tree form for better visualization making it easier to work with
- Disadvantage:
  - it processes the entire file at once so if you are working with a very large file then it could take a bit of time. 

---

# Serialization
- Take any object and convert it to a JSON string
- Serialization is the process in which you take a Java Object and convert it to bytes so that you can do x-number of operations on it. 
- Deserialization is the reverse process.

---

# GSON for the Chess Project
- Sometimes GSON needs help to know how to serialize(write) and/or deserialize(read) an object
- 