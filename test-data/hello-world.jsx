import React from "react";
import ReactDOM from "react-dom";

const App = () => {
  const name = "World";
  return (
    <React.Fragment>
      <h1>Hello {name}!</h1>
      <p>This is the first JSX Element!</p>
      <p>This is a another JSX Element</p>
    </React.Fragment>
  );
};

const rootElement = document.getElementById("root");
ReactDOM.render(<App />, rootElement);
