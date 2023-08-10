<?php

use CloudEvents\V1\CloudEventInterface;
use Google\CloudFunctions\FunctionsFramework;
use Google\Cloud\Storage\StorageClient;

// phpcs:disable
FunctionsFramework::cloudEvent('cleanup', 'cleanup');

// phpcs:enable
function cleanup(CloudEventInterface $event): void
{
    /** @var array|string|false $projectId */
    $projectId = getenv('PROJECT_ID');

    /** @var array|string|false $bucketName */
    $bucketName = getenv('BUCKET_NAME');

    $storage = new StorageClient([ 'projectId' => $projectId ]);

    $clientBucket = $storage->bucket($bucketName);

    /** @var array<mixed,mixed> $eventData */
    $eventData = $event->getData();

    /** @var string $objectName */
    $objectName = $eventData['objectName'];

    $obj = $clientBucket->object($objectName);
    $obj->delete();
}
