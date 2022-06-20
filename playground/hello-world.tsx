import React from "react";
import ReactDOM from "react-dom";

const App = () => {
  return (
    <>
      <section></section>
      <h1>Hello, World!</h1>
      <p>
        I'm baby aesthetic neutra cornhole, austin keytar ethical 3 wolf moon
        flexitarian normcore paleo wolf chillwave. Selvage craft beer direct
        trade pitchfork. Four dollar toast try-hard salvia yr. Viral activated
        charcoal microdosing put a bird on it, prism blue bottle scenester tofu.
        Beard craft beer vegan, edison bulb blue bottle chicharrones
        farm-to-table pug wayfarers selfies man bun.
      </p>
      <p>
        Truffaut twee mixtape typewriter prism hot chicken try-hard. Tote bag
        taxidermy vegan vaporware you probably haven't heard of them vice
        cornhole distillery tilde DSA intelligentsia literally cred raw denim.
        8-bit vinyl ethical kale chips unicorn jean shorts cloud bread four
        loko. Mixtape fingerstache kombucha bushwick ramps.
      </p>
    </>
  );
};

const rootElement = document.getElementById("root");
ReactDOM.render(<App />, rootElement);
