# Open AI plugin for Weather Forecast

In this example, we will create a plugin that uses the Open Weather API to get the weather forecast for a given city.

You may then deploy using the following methods:
- [Deploy to AKS from Github Actions](#1-deploy-to-aks-from-github-actions)
- [Manual deploy image in App Service](#2-manual-deploy-image-in-app-service)

# 1. Deploy to AKS from Github Actions

## Create secrets

```bash
AKS_RESOURCE_GROUP=<resource group>
AKS_CLUSTER_NAME=<cluster name>
AKS_NAMESPACE=chat-copilot-plugins
AZURE_CREDENTIALS=<sp-json>
CLIENT_AZUREOPENAI_KEY=<client-azureopenai-key>
CLIENT_AZUREOPENAI_ENDPOINT=<client-azureopenai-endpoint>
CLIENT_AZUREOPENAI_DEPLOYMENTNAME=<client-azureopenai-deploymentname>
```

# Prepare AKS environment for github actions

## Environment variables

```bash
export RESOURCE_GROUP=<resource group>
export CLUSTER_NAME=<cluster name>
export NAMESPACE=chat-copilot-plugins
```

## Get AKS credentials

```bash
az aks get-credentials --resource-group $RESOURCE_GROUP --name $CLUSTER_NAME
```

## Create namespace

```bash
kubectl create namespace $NAMESPACE
```

# 2.  Manual deploy image in App Service

## Create App Service

```bash
RESOURCE_GROUP=<app-service-rg>
TAG=$(git rev-parse HEAD)
IMAGE=ghcr.io/dsanchor/weather-forecast-oai-plugin:$TAG
az appservice plan create --name chat-copilot-plugins --resource-group $RESOURCE_GROUP --sku S1 --is-linux
az webapp create --resource-group $RESOURCE_GROUP --plan chat-copilot-plugins --name chat-copilot-plugins --deployment-container-image-name $IMAGE
```

## Set environment variables

```bash
DOMAIN=$(az webapp show --name chat-copilot-plugins --resource-group $RESOURCE_GROUP --query defaultHostName --output tsv)
CLIENT_AZUREOPENAI_KEY=<client-azureopenai-key>
CLIENT_AZUREOPENAI_ENDPOINT=<client-azureopenai-endpoint>
CLIENT_AZUREOPENAI_DEPLOYMENTNAME=<client-azureopenai-deploymentname>

az webapp config appsettings set --resource-group $RESOURCE_GROUP --name chat-copilot-plugins --settings OAI_PLUGIN_BASEURL=https://$DOMAIN CLIENT_AZUREOPENAI_KEY=$CLIENT_AZUREOPENAI_KEY CLIENT_AZUREOPENAI_ENDPOINT=$CLIENT_AZUREOPENAI_ENDPOINT CLIENT_AZUREOPENAI_DEPLOYMENTNAME=$CLIENT_AZUREOPENAI_DEPLOYMENTNAME
```

## Redeploy image with latest commit

```bash
TAG=$(git rev-parse HEAD)
IMAGE=ghcr.io/dsanchor/weather-forecast-oai-plugin:$TAG
az webapp config container set --resource-group $RESOURCE_GROUP --name chat-copilot-plugins --docker-custom-image-name $IMAGE
```
