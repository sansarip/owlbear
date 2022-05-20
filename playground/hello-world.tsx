import React from "react";
import ReactDOM from "react-dom";

const App = () => {
  const name = "World";
  return (
    <>
      <h1>
        Hello {name}!
        {...[
          <h2>
            {" "}
            Hello again!, <h3> And again!</h3>
          </h2>,
        ]}
      </h1>
      <p>This is the first JSX Element!</p>
      <p>This is a another JSX Element</p>
    </>
  );
};

const rootElement = document.getElementById("root");
ReactDOM.render(<App />, rootElement);
