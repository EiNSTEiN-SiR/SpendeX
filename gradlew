#!/bin/sh
APP_HOME=$(cd "\${0%/*}" && pwd -P) || exit
exec gradle "$@"
