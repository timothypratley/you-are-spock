# You are Spock

Spock encounters puzzling situations which he must overcome with his only weapon: Logic.

[Play the You are Spock game.](https://timothypratley.github.io/you-are-spock)

![Spock eyebrow raise](https://vignette.wikia.nocookie.net/memoryalpha/images/2/2e/Eyebrow.jpg/revision/latest?cb=20150814183612&path-prefix=en)


## Overview

A game to explore the suitability of the `Entity` abstraction of DataScript.
Built with great [justice](https://www.github.com/timothypratley/justice).


## Setup

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the need to reload.

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`.
You will not get live reloading, nor a REPL.


## Deploying

`./deploy.sh`


## License

Copyright © 2019 Timothy Pratley

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
