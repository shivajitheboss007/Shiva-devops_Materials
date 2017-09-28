package com.pipeline.simple

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.View

class SimplePipelineBuilder {

    DslFactory dslFactory
    String projectName
    String scmPoll
    String repository

    void build(DslFactory dslFactory){

        this.dslFactory = dslFactory
        assemblePipeline()
        createPipelineView()
    }

    private void assemblePipeline(){
        createBuildJob()
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

    private View createPipelineView() {
        dslFactory.deliveryPipelineView(projectName) {
            allowPipelineStart()
            enableManualTriggers()
            pipelines {
                component(projectName, "${projectName}-build")
            }
        }
    }
    }

