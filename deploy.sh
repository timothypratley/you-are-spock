#!/bin/bash
set -e
cd $(dirname $0)
lein do clean, cljsbuild once min
cd resources/public
git init
git add .
git commit -m "Deploy to GitHub Pages"
git push --force --quiet "git@github.com:timothypratley/you-are-spock.git" master:gh-pages
rm -fr .git
echo "Deployed to https://timothypratley.github.io/you-are-spock"
