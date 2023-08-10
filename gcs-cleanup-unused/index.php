<?php

use CloudEvents\V1\CloudEventInterface;
use Google\CloudFunctions\FunctionsFramework;
use Google\Cloud\Storage\StorageClient;

$projectId = getenv('PROJECT_ID');
$bucketName = getenv('BUCKET_NAME');

$storage = new StorageClient([ 'projectId' => $projectId ]);

$clientBucket = $storage->bucket($bucketName);

FunctionsFramework::cloudEvent('cleanup', 'cleanup');

function cleanup(CloudEventInterface $event): void 
{
    /** @var array<mixed,mixed> $eventData */
    $eventData = $event->getData();
    
    /** @var string $objectName */
    $objectName = $eventData['objectName'];

    global $clientBucket;

    $obj = $clientBucket->object($objectName);
    $obj->delete();
}