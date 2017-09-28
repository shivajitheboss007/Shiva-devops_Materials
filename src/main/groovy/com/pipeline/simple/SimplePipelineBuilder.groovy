package com.pipeline.simple

import javaposse.jobdsl.dsl.DslFactory

class SimplePipelineBuilder {

    DslFactory dslFactory
    String projectName
    String scmPoll
    String repository

    void build(DslFactory dslFactory){

        this.dslFactory = dslFactory;
    }

    private void assemblePipeline(){

    }

    private void createBuildJob(){
        dslFactory.job("${projectName}-build") {
            scm {
                git repository
            }
            triggers {
                scm scmPoll
            }
            steps {
                gradle {
                    tasks 'clean publish'
                    useWrapper()
                }
            }

        }
    }
    }

