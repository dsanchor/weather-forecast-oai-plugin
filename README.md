# Open AI plugin for Weather Forecast

# Github

## Create secrets

AKS_RESOURCE_GROUP=<resource group>
AKS_CLUSTER_NAME=<cluster name>
AKS_NAMESPACE=chat-copilot-plugins
AZURE_CREDENTIALS=<sp-json>

# Prepare AKS environment

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