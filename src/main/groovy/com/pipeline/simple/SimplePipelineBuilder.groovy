package com.pipeline.simple

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.View
import javaposse.jobdsl.dsl.helpers.publisher.PublisherContext

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
        createDeployOnTestJob()
    }
    private Job createBuildJob() {
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
            publishers {
                triggerDownstreamJob(delegate, "${projectName}-deploy-on-test", [VERSION: '$GIT_COMMIT'])
            }
        }
    }

    private Job createDeployOnTestJob() {
        dslFactory.job("${projectName}-deploy-on-test") {
            steps {
                shell 'echo "deploying version $VERSION on test"'
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

    void triggerDownstreamJob(PublisherContext publisherContext, String downstreamJob, Map downstreamParameters = [:]) {
        publisherContext.downstreamParameterized {
            trigger(downstreamJob) {
                condition 'SUCCESS'
                parameters {
                    currentBuild()
                    if (downstreamParameters) {
                        predefinedProps downstreamParameters
                    }
                }
            }
        }
    }
    }

