#!/bin/bash
mvn clean install -Dtest=false -DfailIfNoTests=false eclipse:clean eclipse:eclipse -U

exit 0
